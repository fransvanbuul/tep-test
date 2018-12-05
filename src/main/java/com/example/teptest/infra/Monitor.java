package com.example.teptest.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.EventProcessor;
import org.axonframework.eventhandling.EventTrackerStatus;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@RequiredArgsConstructor
public class Monitor {

    private final DataSource dataSource;
    private final EventProcessingConfiguration eventProcessingConfiguration;
    private final Set<String> ignoredProcessors = new CopyOnWriteArraySet<>();

    @Scheduled(fixedRate=5000)
    public void on() {
        logEventTable();
        logProcessor("processor1");
        logProcessor("processor2");
    }

    private void logEventTable() {
        if (log.isDebugEnabled()) {
            try(Connection connection = dataSource.getConnection()) {
                String sql = "SELECT COUNT(*), MAX(globalindex) FROM domainevententry";
                try(ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
                    if(rs.next()) {
                        log.debug("event count {}, max global index {}", rs.getInt(1), rs.getInt(2));
                    } else {
                        log.error("no data");
                    }
                }
            } catch(SQLException ex) {
                log.error("SQLException", ex);
            }
        }
    }

    private void logProcessor(String processingGroup) {
        if(log.isDebugEnabled() && !ignoredProcessors.contains(processingGroup)) {
            Optional<EventProcessor> processor = eventProcessingConfiguration.eventProcessorByProcessingGroup(processingGroup);
            if (processor.isPresent()) {
                TrackingEventProcessor tep = (TrackingEventProcessor) processor.get();
                Map<Integer, EventTrackerStatus> statusMap = tep.processingStatus();
                StringBuilder sb = new StringBuilder();
                sb.append("TEP for processing group <").append(processingGroup).append(">:");
                if (statusMap.entrySet().isEmpty()) {
                    sb.append("  no threads active");
                } else {
                    for (Map.Entry<Integer, EventTrackerStatus> entry : statusMap.entrySet()) {
                        sb.append("\n\t thread ").append(entry.getKey());
                        sb.append(" token ").append(entry.getValue().getTrackingToken());
                    }
                }
                log.debug(sb.toString());
            } else {
                log.debug("No TEP for processing group <{}>, stopping monitoring", processingGroup);
                ignoredProcessors.add(processingGroup);
            }
        }
    }

}

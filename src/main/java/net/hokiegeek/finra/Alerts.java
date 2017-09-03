package net.hokiegeek.finra;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.hokiegeek.finra.store.FileRecord;
import net.hokiegeek.finra.store.FileStore;

@Component
public class Alerts {
    private static final Logger log = LoggerFactory.getLogger(Alerts.class);

    private final FileStore store;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private Date lastChecked = new Date();

    @Autowired
    public Alerts(FileStore store) {
        this.store = store;
    }

    @Scheduled(fixedRateString = "${alert.newitem.pollrate.in.milliseconds}")
    public void pollForNewItems() {
        List<FileRecord> newItems = this.store.getAllRecords().stream()
            .filter(e -> e.getStoredTimestamp().after(lastChecked))
            .collect(Collectors.toList());

        log.info("{}: {} new records out of {}", dateFormat.format(new Date()),
                                                 newItems.size(),
                                                 this.store.count());
        if (!newItems.isEmpty()) {
            sendNewItemAlert(newItems);
        }

        lastChecked = new Date();
    }

    public static void sendNewItemAlert(List<FileRecord> items) {
            log.info("TODO: Send alert!!");
    }
}

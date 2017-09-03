package net.hokiegeek.finra;

import java.lang.StringBuffer;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.hokiegeek.finra.store.FileRecord;
import net.hokiegeek.finra.store.FileStore;

@Component
public class Alerts {
    private static final Logger log = LoggerFactory.getLogger(Alerts.class);

    private final FileStore store;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Date lastChecked = new Date();

    @Value("${alert.newitem.email}")
    private String recipientAddress;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    public Alerts(FileStore store) {
        this.store = store;
    }

    @Scheduled(fixedRateString = "${alert.newitem.pollrate.in.milliseconds}")
    public void pollForNewItems() {
        // Retrieve all stored records and filter out any which are older than
        // the last time we checked for changes
        List<FileRecord> newItems = this.store.getAllRecords().stream()
            .filter(e -> e.getStoredTimestamp().after(lastChecked))
            .collect(Collectors.toList());

        log.info("{}: {} new records out of {}", dateFormat.format(new Date()),
                                                 newItems.size(),
                                                 this.store.count());
        if (!newItems.isEmpty()) {
            sendNewItemAlert(emailSender, recipientAddress, newItems, lastChecked);
        }

        lastChecked = new Date();
    }

    public static void sendNewItemAlert(JavaMailSender sender, String recipient,
                                        List<FileRecord> items, Date previousCheckedDate) {
        log.info("Sending email alert");

        StringBuffer text = new StringBuffer();
        text.append("The following ");
        text.append(items.size());
        text.append(" files were added to the data store since ");
        text.append(dateFormat.format(previousCheckedDate));
        text.append("\n");
        items.stream().forEach(e -> {
                text.append(e.getOriginalFilename());
                text.append(" (");
                text.append(e.getId());
                text.append(")\n");
            });

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@finra.org");
        message.setTo(recipient);
        message.setSubject("[finra-test] New files added to storage");
        message.setText(text.toString());

        sender.send(message);
    }
}

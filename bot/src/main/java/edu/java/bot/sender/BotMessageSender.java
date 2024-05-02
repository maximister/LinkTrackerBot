package edu.java.bot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BotMessageSender {
    private final TelegramBot bot;
    private final Counter counter;

    public BotMessageSender(TelegramBot bot, MeterRegistry meterRegistry) {
        this.bot = bot;
        counter = Counter.builder("processed_messages")
            .description("Total number of processed messages")
            .register(meterRegistry);
    }

    public void sendMessage(SendMessage message) {
        counter.increment();
        bot.execute(message.parseMode(ParseMode.Markdown));
    }
}

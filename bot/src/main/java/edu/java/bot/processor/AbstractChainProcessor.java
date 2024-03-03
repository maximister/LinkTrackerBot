package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

/**
 * Сообщение может содержать не только message, но и другие типы.
 * Для возможной работы с ними в будующем сделал processor  виде цепи,
 * чтобы облегчить расширение
 */
public abstract class AbstractChainProcessor implements Processor {
    private AbstractChainProcessor next;

    public static AbstractChainProcessor makeChain(AbstractChainProcessor first, AbstractChainProcessor... chain) {
        AbstractChainProcessor head = first;
        for (AbstractChainProcessor nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    protected SendMessage checkNext(Update update) {
        if (next == null) {
            throw new RuntimeException("Bad request");
        }
        return next.check(update);
    }
}

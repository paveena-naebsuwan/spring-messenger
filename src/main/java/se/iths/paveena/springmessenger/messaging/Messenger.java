package se.iths.paveena.springmessenger.messaging;

import se.iths.paveena.springmessenger.model.Message;

public interface Messenger {
    void send(Message message);
}

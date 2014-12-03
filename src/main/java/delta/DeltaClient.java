package delta;

import org.reactivestreams.Publisher;

public interface DeltaClient {

    Publisher<Message> consume();

}

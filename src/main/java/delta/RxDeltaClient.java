package delta;

import org.reactivestreams.Publisher;
import rx.Observable;
import rx.Producer;
import rx.RxReactiveStreams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RxDeltaClient implements DeltaClient {

    @Override
    public Publisher<Message> consume() {
        return RxReactiveStreams.toPublisher(Observable.create(subscriber -> {

            Iterator<Message> it = createMessageIterator();
            AtomicLong i = new AtomicLong(0);

            subscriber.setProducer(l -> {
                long val = i.addAndGet(l);
                 while (val > 0 && !subscriber.isUnsubscribed()) {
                     i.decrementAndGet();
                     subscriber.onNext(it.next());
                 }
            });
        }));
    }

    private Iterator<Message> createMessageIterator() {
        return new Iterator<Message>() {

            int count = 0;
            Random r = new Random();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Message next() {
                count++;
                Map<Integer, Integer> offsets = new HashMap();
                offsets.put(r.nextInt(4), count);
                return new Message(offsets, "message payload " + count);
            }
        };
    }
}

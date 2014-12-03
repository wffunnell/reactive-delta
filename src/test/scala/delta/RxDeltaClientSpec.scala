package delta

import java.util

import org.reactivestreams.Publisher
import org.scalatest.{FlatSpec, Matchers}
import rx.functions.Func2
import rx.{RxReactiveStreams, Subscriber, Subscription}


class RxDeltaClientSpec extends FlatSpec with Matchers {

  def client = new RxDeltaClient

  "Rx Delta Client" should "give me 10 thingies" in {

    val consume: Publisher[Message] = client.consume()

    val subscribe: Subscription = RxReactiveStreams
      .toObservable(consume)
      .take(10)
      .scan(new Func2[Message, Message, Message] {
      override def call(t1: Message, t2: Message): Message = {
        val map: util.HashMap[Integer, Integer] = new util.HashMap[Integer, Integer]
        map.putAll(t1.getOffsets)
        map.putAll(t2.getOffsets)
        new Message(map, t2.getPayload)
      }
    })
      .subscribe(CountingObserver)

    subscribe.unsubscribe()

    CountingObserver.count should be(10)
  }

}

object CountingObserver extends Subscriber[Message] {
  var counter = 0

  override def onStart(): Unit = request(1)

  override def onCompleted(): Unit = ???

  override def onError(throwable: Throwable): Unit = ???

  override def onNext(t: Message): Unit = {
    println(t)
    counter+=1
    request(1)
  }

  def count: Int = counter
}

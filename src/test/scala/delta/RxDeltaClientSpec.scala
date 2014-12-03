package delta

import org.reactivestreams.Publisher
import org.scalatest.{Matchers, FlatSpec}
import rx.{Subscription, RxReactiveStreams, Subscriber}

import scala.collection.parallel

class RxDeltaClientSpec extends FlatSpec with Matchers {

  def client = new RxDeltaClient

  "Rx Delta Client" should "give me 10 thingies" in {

    val consume: Publisher[Message] = client.consume()

    val subscribe: Subscription = RxReactiveStreams
      .toObservable(consume)
      .take(10)
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

package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {

  //part1 - actor systems
  val actorSystem=ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // part2 - create actors
  // word count actor
  class WordCountActor extends Actor {
    // internal data
    var totalWords=0

    //behavior
    override def receive: PartialFunction[Any,Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords+=(" ").length
      case msg=> println(s"[word counter] I cannot undestand ${msg.toString}")
    }
  }

  //part3 - instantiate our actor
  val wordCounter= actorSystem.actorOf(Props[WordCountActor],"wordCounter")
  val anotherWordCounter= actorSystem.actorOf(Props[WordCountActor],"anotherWordCounter")
  //part4- communicate!
  wordCounter ! "I am learning Akka and it's pretty damn cool!" // ! -> "tell"
  wordCounter ! "A different message"
  //asynchronous

  object Person{
    def props(name:String): Props =Props(new Person(name))
  }
  class Person(name:String)extends Actor{
    override def receive:Receive= {
      case "hi"=>println(s"Hi, my name is $name")
      case _ =>
    }
  }

  //best practice is to define a companion object define a props method with its arguments on constructor
  val person=actorSystem.actorOf(Person.props("Bob")) //this instantiation is legal
  person ! "hi"





}

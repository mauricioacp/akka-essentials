package part1recap

import scala.concurrent.Future


object AdvancedRecap extends App {

  //partial functions
  val partialFunction:PartialFunction[Int,Int]={
    case 1=>42
    case 2=>65
    case 3=>99
  }

  val pf=(x:Int)=> x match {
    case 1=>42
    case 2=>65
    case 3=>99
  }

  val function:(Int=>Int) = partialFunction

  val modifiedList = List(1,2,3).map({
    case 1 => 42
    case _ => 0
  })

  //lifting
  val lifter=partialFunction.lift //total function Int=> Option[Int]
  lifter(2) //Some(65)
  lifter(5000)//None

  //orElse
  val pfChain=partialFunction.orElse[Int,Int]{
    case 60 => 9000
  }

//  pfChain(5) // 999 per partialFunction
  pfChain(60) // 9000
//  pfChain(457) // throw a MatchError

  //type aliases
  type ReceiveFunction = PartialFunction[Any,Unit]

  def receive:ReceiveFunction ={
    case 1 => println("Hello")
    case _ => println("Confused...")
  }

  // implicits
  implicit val timeOut=3000
  def setTimeOut(f:()=>Unit)(implicit timeOut:Int): Unit =f()

  setTimeOut(()=>println("timeout")) //extra parameter list omitted

  // impicit conversions
  //1) implicit defs
  case class Person(name:String){
    def greet=s"hi my name is $name"
  }

  implicit def fromStringToPerson(string:String):Person=Person(string)

  "Peter".greet
  //fromStringToPerson("Peter).greet - automatically by the compiler

  // 2) implicit classes
  implicit class Dog(name:String){
    def bark=println("bark!")
  }

  "Lassie".bark

  // new Dog("Lassie").bark - automatically done by the compiler

  //organize
  //Local scope
  implicit val inverseOrdering:Ordering[Int]=Ordering.fromLessThan(_ > _)
  List(1,2,3).sorted //List (3,2,1)

  // imported scope
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future{
    println("Hello future")
  }

  // companion objects of the types included in the call
  object Person{
    implicit val personOrdering:Ordering[Person]=Ordering.fromLessThan((a,b)=> a.name.compareTo(b.name)<0)
  }

  //the compiler will fetch the implicit value
  //because is it in the companion object of Person
  //which is a type involved in the method call
  List(Person("Bob"),Person("Alice")).sorted
  //List(Person(Alice),Person(Bob))


}

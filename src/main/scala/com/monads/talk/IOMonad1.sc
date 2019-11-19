
def calculateTax(income: Double, tax: Double): Double =
//calls tax web service
  income + tax

def saveTaxCalculation(taxValue: Double): Unit =
  println(s"tax saved $taxValue")

def sendMessageToUser(taxValue: Double): Unit =
  println(s"Message sent: Total Tax $taxValue")

//run the program
def runProgram(): Unit = {
  val taxValue = calculateTax(15.2, 5.5)
  saveTaxCalculation(taxValue)
  sendMessageToUser(taxValue)
}

/*

program{
    run(){
      doA() //side effect
      doB() //side effect
      doC() //side effect
    }
}


doC(doB(doA()))


*/




sealed trait IO[A] {
  def flatMap[B](f: A => IO[B]): IO[B]

  def map[B](f: A => B): IO[B]

  def run: A
}

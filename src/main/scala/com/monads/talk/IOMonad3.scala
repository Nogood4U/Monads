package com.monads.talk

object IOMonad3 {

  def main(args: Array[String]): Unit = {


    def calculateTax(income: Double, tax: Double): IO[Double] =
      CallTaxEndPoint(income, tax)

    def saveTaxCalculation(taxValue: Double): IO[Unit] =
      Println(s"tax saved $taxValue")

    def sendMessageToUser(taxValue: Double): IO[Unit] =
      Println(s"Message sent: Total Tax $taxValue")


    //Represent Actions as Data

    def Println(msg: String): IO[Unit] = IO(println(msg))

    def CallTaxEndPoint(income: Double, tax: Double):IO[Double] = IO(income + tax)


    //run the program
    val program = calculateTax(15.2, 5.5)
      .flatMap(taxValue => {
        saveTaxCalculation(taxValue)
          .flatMap(_ => {
            sendMessageToUser(taxValue)
          })
      })

    program.run
    println("-----------------------")


    val program2 = for {
      taxValue <- calculateTax(15.2, 5.5)
      _ <- saveTaxCalculation(taxValue)
      _ <- sendMessageToUser(taxValue)
    } yield ()

    program2.run

    println("-----------------------")

    val program3 = for {
      taxValue <- calculateTax(15.2, 5.5)
      _ <- saveTaxCalculation(taxValue)
      modifiedTaxValue = taxValue * 2
      _ <- IO(println(s"taxModified, new tax value: $modifiedTaxValue"))
      _ <- sendMessageToUser(modifiedTaxValue)
    } yield ()

    println(program3)

    program3.run
  }
}

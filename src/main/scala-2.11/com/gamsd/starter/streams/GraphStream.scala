package com.gamsd.starter.streams

import akka.stream.ClosedShape
import akka.stream.scaladsl._
import com.gamsd.starter.AkkaElements

import scala.util.{Success, Failure}

object GraphStream extends AkkaElements with App {

  def pingPong(in: Int): (Int, Option[String]) = in match {
    case i if i % 3 == 0 && i % 5 == 0 => (i, Some(s"PingPong $i"))
    case i if (i % 3) == 0 => (i, Some(s"Ping $i"))
    case i if (i % 5) == 0 => (i, Some(s"Pong $i"))
    case i => (i, None)
  }

  private val graph = RunnableGraph.fromGraph(GraphDSL.create(Sink.ignore) { implicit builder =>
    sink =>

    import GraphDSL.Implicits._

    val in = Source(1 to 25)

    val f1 = Flow[Int]
      .map(pingPong)

    val bcast = builder.add(Broadcast[(Int, Option[String])](2))

    val fOk  = Flow[(Int, Option[String])] filter (_._2.isDefined) map (_._2.get) map System.out.println
    val fNok = Flow[(Int, Option[String])] filter (_._2.isEmpty) map (x => s"I don't care for ${x._1}") map System.err.println

    val merge = builder.add(Merge[Unit](2))

    val out = Sink.ignore

    in ~> f1 ~> bcast ~> fOk  ~> merge ~> sink.in
                bcast ~> fNok ~> merge

    ClosedShape
  })

  graph.run().onComplete {
    case Success(_) => System.exit(0)
    case Failure(_) => System.exit(1)
  }

}

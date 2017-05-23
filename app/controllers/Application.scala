package controllers

import com.google.inject.Inject
import play.api.mvc._
import service.CakeRepo

import scala.concurrent.ExecutionContext

class Application@Inject() (cakeRepo: CakeRepo)(implicit ec: ExecutionContext) extends Controller {

  def index = Action.async {


    cakeRepo.all.map{ cakes =>
      Ok(views.html.index("Cakes! " + cakes.toString()))
    }

  }

}
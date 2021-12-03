import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.util.UUID.randomUUID


class SearchBookConsultSimulation extends Simulation {

    private val baseURL = sys.env("GATLING_TARGET_URL")
    private val nbUsers = sys.env("GATLING_RAMP_USERS").toInt
    private val rampDuration = sys.env("GATLING_RAMP_DURATION").toInt
    private val acceptHeader = "application/json"
    private val acceptEncodingHeader = "gzip, deflate"
    private val userAgentHeader = "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0"
    private val SEARCH_URL = "/api/travels/search"

    val httpProtocol = http
    .baseUrl(baseURL)
    .acceptHeader(acceptHeader)
    .acceptEncodingHeader(acceptEncodingHeader)
    .userAgentHeader(userAgentHeader)

    def searchTrip(from: String, to: String, fromStation: String,
    toStation: String, date: String)= exec(
        http(s"Trips from $fromStation to $toStation")
        .get(SEARCH_URL)
        .queryParamSeq(Seq(("from", from), ("to", to), ("date",date)))
    )

    object Search { //scala singleton
        val search= exec(searchTrip("StopArea:OCE87723197", "StopArea:OCE87686006",
    "Lyon Part-Dieu", "Paris Gare de Lyon", "2020-01-11"))
    }  

    val searchScenario= scenario("Search")
    .exec(Search.search) 

    setUp(searchScenario.inject(rampUsers(nbUsers) during(rampDuration))).protocols(httpProtocol)

}
import "source-map-support/register";
import { SharpSpringService, EventRequest } from "./sharpspring-service";
import { EvendtDao } from "./dao/event-dao";
import { POINT_CONVERSION_COMPRESSED } from "constants";

var sharpSpringService = new SharpSpringService();

export const App = async (event, _context) => {
  try {
    if (event.body) {
      //This is for testing. HTTP post request to /test
      //You can see event body in event.json
      let item = JSON.parse(event.body).Records[0];
      await sharpSpringService.processMessage(item);
    } else {
      for (const item of event.Records) await sharpSpringService.processMessage(item);
    }
  } catch (err) {
    console.log("ERROR", err);
  }
};

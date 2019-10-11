import "source-map-support/register";
import { SharpSpringService, EventRequest } from "./sharpspring-service";
import { EvendtDao } from "./dao/event-dao";

var sharpSpringService = new SharpSpringService();

export const App = async (event, _context) => {
  for (const item of event.Records) await sharpSpringService.processMessage(item);
};

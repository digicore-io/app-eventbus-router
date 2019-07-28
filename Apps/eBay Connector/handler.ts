
import 'source-map-support/register';
import { CoreService } from './core-service';
import { Response } from './classes';
import { Router } from './router';

var coreService = new CoreService();

export const App = async (event, _context) => {
 
  if (event.Records) {
    for (const item of event.Records)
      await coreService.processMessage(item);
  } else {
    try {
      
      let router = new Router();
      let result = await router.handleRoute(event);

      return getResponse(new Response(200, true, result, event));
    } catch (err) {
      if (err instanceof Error == false)
        return getResponse(new Response(500, false, "" + err, null))
      else
        return getResponse(new Response(err.status, false, err.message, event));
    }
  }
}

function getResponse(body: any) {
  return {
    statusCode: body.status,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Credentials": true
    },
    body: JSON.stringify(body),
  };
}
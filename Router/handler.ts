
import 'source-map-support/register';
import { QueueService } from './service/queue-service';
import { EvendtDao } from './dao/event-dao';
import { ApiService } from './service/api-service';
import { Router } from './router';
import { Response, HttpError } from './classes';

var queueService = new QueueService();
var apiService = new ApiService();
var eventDao = new EvendtDao();

export const router = async (event, _context) => {
  


  if (event.headers) {  // API Gateway request from external source
    let router = new Router();
    let startTime = new Date().getTime();

    try {

      let result = await router.handleRoute(event);

      let endTime = new Date().getTime();
      return getResponse(new Response(200, true, result, event, endTime - startTime));
    } catch (err) {
      let endTime = new Date().getTime();
      console.log(err)
      if (err instanceof HttpError == false){
        console.log('test')
        return getResponse(new Response(500, false, "" + err, event, endTime - startTime))
      }
      else
        return getResponse(new Response(err.status, false, err.message, event, endTime - startTime));
    }
  } else //SQS Message
    await queueService.processMessages(event);
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
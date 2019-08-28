
import 'source-map-support/register';
import { QueueService } from './service/queue-service';
import { Router } from './router';
import { HttpError, Response } from '../../NodeJS-Common/common-classes';
import { BaseService } from '../../NodeJS-Common/base-service';

var queueService = new QueueService();
var baseService = new BaseService();
const API = 'Eventbus Router';
export const router = async (event, _context) => {
  

  if (event.headers) {  
    // API Gateway request from external source
    let router = new Router();
    let startTime = new Date().getTime();

    try {

      let result = await router.handleRoute(event);

      let endTime = new Date().getTime();
      return getResponse(new Response(200, true, result, event, endTime - startTime,API));
    } catch (err) {
      let endTime = new Date().getTime();
      let response;
      if (err instanceof HttpError == false){
        response = getResponse(new Response(500, false, "" + err, event, endTime - startTime, API))
      }
      else
        response = getResponse(new Response(err.status, false, err.message, event, endTime - startTime, API));

      baseService.logToSlack('devops-event-bus', 'EventBus Router', response);
    }
  } else {
    //SQS Message
    try{
      await queueService.processMessages(event);
    }catch(err){
      baseService.logToSlack('devops-event-bus', 'EventBus Router', err);
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

import 'source-map-support/register';
import { QueueService, EventRequest, Application, DestinationType } from './queue-service';
import { EvendtDao } from './dao/event-dao';

var queueService = new QueueService();
var eventDao = new EvendtDao();

export const router = async (event, _context) => {
  console.log('EVENT', event);
  
  await queueService.processMessages(event);
  
}

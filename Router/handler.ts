
import 'source-map-support/register';
import { QueueService, EventRequest, Application, DestinationType } from './queue-service';

var queueService = new QueueService();

export const router = async (event, _context) => {
  
  await queueService.processMessages(event);

}

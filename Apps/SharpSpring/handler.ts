
import 'source-map-support/register';
import { SharpSpringService, EventRequest} from './sharpspring-service';
import { EvendtDao } from './dao/event-dao';

var sharpSpringService = new SharpSpringService();


export const processor = async (event, _context) => {
  console.log('EVENT', event);
  
  await sharpSpringService.processMessages(event);
  
}

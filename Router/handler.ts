
import 'source-map-support/register';
import { QueueService, EventRequest, Application, DestinationType } from './queue-service';

var queueService = new QueueService();

export const router = async (event, _context) => {
  
  await queueService.processMessages(event);

  // let testEvent:EventRequest = new EventRequest();
  // testEvent.applicationId = "1111";
  // testEvent.companyId = "1232";
  // testEvent.payload = {};
  // testEvent.payload.listId  = 23132;
  
  // let app:Application = new Application();
  // app.destinationType = DestinationType.SQS;
  // app.destinationUrl = "https://sqs.us-west-2.amazonaws.com/951585821496/AppEventBusQueue"
  // await queueService.sendSQS(testEvent, null, app);
  // function wait(){
  //   return new Promise((resolve, reject) => {
  //       setTimeout(() => resolve("hello"), 2000)
  //   });
  // }
  // console.log(await wait());
  // console.log(await wait());
  // console.log(await wait());
  // console.log(await wait());
  // console.log(await wait());
  // console.log(await wait());

  console.log('Exiting')
  
  return "Existing"
}

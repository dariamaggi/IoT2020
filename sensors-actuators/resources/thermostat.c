#include "contiki.h"

#include <stdio.h>
#include <string.h>
#include "time.h"
#include "coap-engine.h"
#include "coap-observe.h"
#include "os/dev/leds.h"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "App"
#define LOG_LEVEL LOG_LEVEL_DBG

#define UPPER_THRESHOLD   25
#define LOWER_THRESHOLD		22
#define PERIODIC_HANDLER_INTERVAL 30

static double current_temperature = 23.5;
extern bool cooling_active;
static int count = 0;


static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

double temperature_gap_generator(double lower, double upper) 
{ 
  double gap = (rand() %  (upper - lower + 1)) + lower; 
  return gap;
} 


EVENT_RESOURCE(thermostat,
   "title=\"Local temperature\";obs;rt=\"Temperature Sensor\"",
   res_get_handler,
   NULL,
   NULL,
   NULL,
   res_event_handler);

static void res_event_handler(void){
  count ++;
  coap_notify_observers(&thermostat);
}


static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset){
  if(request != NULL) {
    LOG_DBG("Observing handler: %d\n", count);
  }
  
  current_temperature = temperature_gap_generator(22, 30);

  if(current_temperature < LOWER_THRESHOLD){
    if(!cooling_active){
      LOG_DBG("Cooling system started automatically\n");
      cooling_active = 1;
      leds_set(LEDS_NUM_TO_MASK(LEDS_GREEN));
    }
  } else if(current_temperature > UPPER_THRESHOLD){
    if(cooling_active){
      LOG_DBG("Cooling system stopped automatically\n");
      cooling_active = 0;
      leds_set(LEDS_NUM_TO_MASK(LEDS_RED));
    }
  }

  unsigned int accept = -1;
  coap_get_header_accept(request, &accept);
  //TOGLIERE XML
  if (accept == -1)
    accept = APPLICATION_JSON;

  if(accept == APPLICATION_XML) {
    coap_set_header_content_format(response, APPLICATION_XML);
    snprintf((char *)buffer, COAP_MAX_CHUNK_SIZE, "<temperature=\"%d\"/>", humidity_value);
    coap_set_payload(response, buffer, strlen((char *)buffer));
    
  } else if(accept == APPLICATION_JSON) {
    coap_set_header_content_format(response, APPLICATION_JSON);
    snprintf((char *)buffer, COAP_MAX_CHUNK_SIZE, "{\"temperature\":%d}", humidity_value);
    coap_set_payload(response, buffer, strlen((char *)buffer));
    
  } else {
    coap_set_status_code(response, NOT_ACCEPTABLE_4_06);
    const char *msg = "Supporting content-type application/json";
    coap_set_payload(response, msg, strlen(msg));
  }
}
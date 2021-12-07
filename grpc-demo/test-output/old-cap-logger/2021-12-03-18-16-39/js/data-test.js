var testSuits = {
  "success": 1,
  "fail": 0,
  "skip": 0,
  "total": 1,
  "starttime": "2015-01-04 15:07:15",
  "endtime": "2015-01-04 15:07:15",
  "driverType": "HttpClient",
  "platform": "PC",
  "suits": [
    {
      "suitname": "Default suite",
      "success": 1,
      "fail": 0,
      "skip": 0,
      "total": 1,
      "starttime": "2015-01-04 15:07:15",
      "endtime": "2015-01-04 15:07:15",
      "testclass": [
        {
          "classname": "com.ctrip.cap.api.test.ActivityServiceTest",
          "cases": [
            {
              "casename": "testAuth",
              "result": 1,
              "log": [
                {
                  "type": "Step",
                  "message": "http://127.0.0.1/Activity-Booking-OpenAPI/ActivityRecommandQOC?",
                  "time": "2015-01-04 15:07:15\n"
                },
                {
                  "type": "File",
                  "message": "p2;p3",
                  "time": "2015-01-04 15:07:15\n"
                },
                {
                  "type": "File",
                  "message": "p4;p5",
                  "time": "2015-01-04 15:07:15\n"
                }
              ],
              "exception": "",
              "starttime": "2015-01-04 15:07:15",
              "endtime": "2015-01-04 15:07:15"
            }
          ]
        }
      ]
    }
  ],
  "filesMap": {
    "p5": {
      "payload": "{\"ResponseStatus\":{\"Timestamp\":\"\\/Date(1420355235651+0800)\\/\",\"Ack\":\"Success\",\"Errors\":[],\"Extension\":[]},\"RowCount\":0,\"ActivityRecommandList\":[],\"DebugInfo\":\"\"}",
      "format": "json"
    },
    "p4": {
      "payload": "{\"ResponseStatus\":{\"Timestamp\":\"\\/Date(1420355235651+0800)\\/\",\"Ack\":\"Success\",\"Errors\":[],\"Extension\":[]},\"RowCount\":0,\"ActivityRecommandList\":[],\"DebugInfo\":\"\"}",
      "format": "json"
    },
    "p3": {
      "payload": "{\"ResponseStatus\":{\"Timestamp\":\"\\/Date(1420355235651+0800)\\/\",\"Ack\":\"Success\",\"Errors\":[],\"Extension\":[]},\"RowCount\":0,\"ActivityRecommandList\":[],\"DebugInfo\":\"\"}",
      "format": "json"
    },
    "p2": {
      "payload": "{  \"ProductionLineID\":0,  \"CityID\":0,  \"RecordLimit\":0,  \"ImageSize\":\"String\",  \"DistributionChannelID\":0,  \"AdditionalData\":[    {      \"Key\":0,      \"Value\":\"String\"    }  ]}\n",
      "format": "json"
    }
  }
}
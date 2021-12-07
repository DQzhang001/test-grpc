var testSuits = {
  "success": 0,
  "fail": 1,
  "skip": 0,
  "total": 1,
  "starttime": "2021-12-03 18:07:03",
  "endtime": "2021-12-03 18:07:20",
  "driverType": "HttpClient",
  "platform": "PC",
  "suits": [
    {
      "suitname": "Default Suite",
      "success": 0,
      "fail": 1,
      "skip": 0,
      "total": 1,
      "starttime": "2021-12-03 18:07:03",
      "endtime": "2021-12-03 18:07:20",
      "testclass": [
        {
          "classname": "com.convertlab.testdemos.demoClient",
          "cases": [
            {
              "casename": "helloClient",
              "caseId": "06148650cf4ef84e35588bb25c702600",
              "TestRailName": "helloClient",
              "result": 2,
              "log": [],
              "exception": "io.grpc.StatusRuntimeException: INVALID_ARGUMENT: Name not found\n\tat io.grpc.stub.ClientCalls.toStatusRuntimeException(ClientCalls.java:262)\n\tat io.grpc.stub.ClientCalls.getUnchecked(ClientCalls.java:243)\n\tat io.grpc.stub.ClientCalls.blockingUnaryCall(ClientCalls.java:156)\n\tat com.convertlab.grpc.GreeterGrpc$GreeterBlockingStub.sayHello(GreeterGrpc.java:169)\n\tat com.convertlab.service.DemoService.sayHello(DemoService.java:30)\n\tat com.convertlab.testdemos.demoClient.helloClient(demoClient.java:18)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.testng.internal.MethodInvocationHelper.invokeMethod(MethodInvocationHelper.java:85)\n\tat org.testng.internal.MethodInvocationHelper$1.runTestMethod(MethodInvocationHelper.java:196)\n\tat org.springframework.test.context.testng.AbstractTestNGSpringContextTests.run(AbstractTestNGSpringContextTests.java:175)\n\tat org.testng.internal.MethodInvocationHelper.invokeHookable(MethodInvocationHelper.java:208)\n\tat org.testng.internal.Invoker.invokeMethod(Invoker.java:635)\n\tat org.testng.internal.Invoker.invokeTestMethod(Invoker.java:821)\n\tat org.testng.internal.Invoker.invokeTestMethods(Invoker.java:1131)\n\tat org.testng.internal.TestMethodWorker.invokeTestMethods(TestMethodWorker.java:125)\n\tat org.testng.internal.TestMethodWorker.run(TestMethodWorker.java:108)\n\tat org.testng.TestRunner.privateRun(TestRunner.java:773)\n\tat org.testng.TestRunner.run(TestRunner.java:623)\n\tat org.testng.SuiteRunner.runTest(SuiteRunner.java:357)\n\tat org.testng.SuiteRunner.runSequentially(SuiteRunner.java:352)\n\tat org.testng.SuiteRunner.privateRun(SuiteRunner.java:310)\n\tat org.testng.SuiteRunner.run(SuiteRunner.java:259)\n\tat org.testng.SuiteRunnerWorker.runSuite(SuiteRunnerWorker.java:52)\n\tat org.testng.SuiteRunnerWorker.run(SuiteRunnerWorker.java:86)\n\tat org.testng.TestNG.runSuitesSequentially(TestNG.java:1185)\n\tat org.testng.TestNG.runSuitesLocally(TestNG.java:1110)\n\tat org.testng.TestNG.run(TestNG.java:1018)\n\tat com.intellij.rt.testng.IDEARemoteTestNG.run(IDEARemoteTestNG.java:66)\n\tat com.intellij.rt.testng.RemoteTestNGStarter.main(RemoteTestNGStarter.java:109)\n",
              "starttime": "2021-12-03 18:07:04",
              "endtime": "2021-12-03 18:07:20",
              "EsErrorLogCount": 0
            }
          ]
        }
      ]
    }
  ]
}
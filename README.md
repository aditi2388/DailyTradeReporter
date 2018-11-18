# DailyTradeReporter

Assumptions:

1. Reading instructions from a comma delimited file, where first line is header.
2. Ranking of the Incoming and Outgoing trade is based on the total Incoming/OutGoing amount irrespective of settlement date of trades

How to Run:

Run the DailyTradeReportGenerator as Java program, which will pick the input from test file trade_input.txt

Test Run:

1. To run the complete program with different inputs-->  DailyTradeReportGeneratorTest.java

which has currently 4 test cases who works with the test_input1.text,test_input2.text,test_input3.text,test_input4.text respectively.
Similarly more test cases can be added and run as required with the similar input format.

2. To test the individual methods--> TradeServiceImpl.java

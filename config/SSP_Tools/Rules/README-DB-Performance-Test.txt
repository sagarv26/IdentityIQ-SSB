This rule evaluates the performance seen between an IdentityIQ application 
server and the database server supporting IdentityIQ.  It does this by
persisting blocks of data into the IdentityIQ database and measuring how long
the system takes to store the data in the database.  

There are 3 sizes of records we store: 1k, 4k, and 8k.  These are designed
to emulate the size of Link, Identity, and Certification items, respectively.
The I/O transactions for these types of items are key items that effect the
performance of IdentityIQ. 

When run the rule will produce output measuring the throughput to the 
database.  The report will look like this, all of the times are in 
milliseconds:

"""
IdentityIQ Database Performance Test
Host: ahampton-mbp-2.local
Date: 2013-10-07 10:49:19.428
HashMap data sets allocated.
Populating 1k, 4k, 8k data set HashMaps for 1000 records...
Data set HashMaps populated.
Testing 1k data set...
Completed 1k data set.
Testing 4k data set...
Completed 4k data set.
Testing 8k data set...
Completed 8k data set.
Meter Generate-IIQDB-Test-DataSets:  1 calls, 73778 milliseconds, 73778 minimum, 73778 maximum, 73778 average
Meter IIQDB-Test-DataSet-1k-All:     1 calls,  7635 milliseconds,  7635 minimum,  7635 maximum,  7635 average
Meter IIQDB-Test-DataSet-1k-Item: 1000 calls,  7608 milliseconds,     3 minimum,    50 maximum,     7 average
Meter IIQDB-Test-DataSet-4k-All:     1 calls, 13071 milliseconds, 13071 minimum, 13071 maximum, 13071 average
Meter IIQDB-Test-DataSet-4k-Item: 1000 calls, 13040 milliseconds,     8 minimum,   534 maximum,    13 average
Meter IIQDB-Test-DataSet-8k-All:     1 calls, 16394 milliseconds, 16394 minimum, 16394 maximum, 16394 average
Meter IIQDB-Test-DataSet-8k-Item: 1000 calls, 16370 milliseconds,    12 minimum,   157 maximum,    16 average
Cleaning up test objects in the database...
... done deleting DB Performance Test records.
"""

Known Performance Benchmarks from Reference Systems (smaller numbers are better):

 System Name     | 1k avg | 4k avg | 8k avg |  Gen Test |    
=================|========|========|========|===========|
2011 15" MBP,hdd |   7 ms | 13 ms  | 16 ms  |  73778 ms |
2014 15" MBP,ssd |   2 ms |  4 ms  |  5 ms  |  34572 ms |
2012 Lenovo T520 |  18 ms | 20 ms  | 14 ms  |  52775 ms |
R630 Appliance 1 |  10 ms | 17 ms  | 21 ms  |  44936 ms |
R630 Appliance 2 |   9 ms | 16 ms  | 19 ms  |  45268 ms |
PerfLab VMs      |   7 ms |  9 ms  | 12 ms  |  60568 ms |
Stg-Ax           |  14 ms | 29 ms  | 39 ms  | 601575 ms |
Prd-Ax           |  23 ms | 55 ms  | 77 ms  | 844358 ms | 
Stry             |   9 ms | 17 ms  | 25 ms  |  93505 ms |

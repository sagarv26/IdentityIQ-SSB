Multi-Threaded Custom Task Kit
Original Release: 20141103, SailPoint Services.
=============================================================================

Release Notes:


20141103 - Initial release.  Support for IdentityIQ 6.2, 6.3+.  

Import all of the attached XML artifacts. 

To use the mutli-threaded custom task you need to create 2 rules: first a 
“queue builder” that puts what you want to chew through on a queue and second 
an “item processor rule” that takes the “workItem” that the queue builder put 
on the queue and processes it.  The "workItem" objects these are strings, like 
Link IDs or Identity names, etc., but could be custom strings like "add|xxx" or
actions like that.

In the UI you can go to Monitor -> Tasks -> New -> MultiThreaded Custom Task.
This presents a form for you to configure in the UI.

Give it a name, select your rules in the drop downs, save and fire.
A No-op example queue builder and item processor rule are included.

It takes the items in the queue and distributes them among N parallel 
instances of the item processor rule. Where N is configurable, but defaults to 
2x the number of cores on the task server running the task,

Since it uses a task definition to kick it off, it’s completely schedule-able.

If you ever need to chew through lots of data quickly this is a way to do it.  
The framework rule takes care of all the thread pool creation, dispatch, error 
catching, and statistics for you.

Two additional rule types: an Initializer rule and a Cleanup rule are optional.
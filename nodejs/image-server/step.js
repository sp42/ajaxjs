// Hook into commonJS module systems
if (typeof module !== 'undefined' && "exports" in module) {
  var define = function (fn){
    fn(null, exports, module);
  }
}

define(function(require, exports, module) {
  function Step() {
    var steps = Array.prototype.slice.call(arguments),
        pending, counter, results, lock;

    // Define the main callback that's given as `this` to the steps.
    function next() {
      counter = pending = 0;

      // Check if there are no steps left
      if (steps.length === 0) {
        // Throw uncaught errors
        if (arguments[0]) {
          throw arguments[0];
        }
        return;
      }

      // Get the next step to execute
      var fn = steps.shift();
      results = [];

      // Run the step in a try..catch block so exceptions don't get out of hand.
      try {
        lock = true;
        var result = fn.apply(next, arguments);
      } catch (e) {
        // Pass any exceptions on through the next callback
        next(e);
      }

      if (counter > 0 && pending == 0) {
        // If parallel() was called, and all parallel branches executed
        // synchronously, go on to the next step immediately.
        next.apply(null, results);
      } else if (result !== undefined) {
        // If a synchronous return is used, pass it to the callback
        next(undefined, result);
      }
      lock = false;
    }

    // Add a special callback generator `this.parallel()` that groups stuff.
    next.parallel = function () {
      var index = 1 + counter++;
      pending++;

      return function () {
        pending--;
        // Compress the error from any result to the first argument
        if (arguments[0]) {
          results[0] = arguments[0];
        }
        // Send the other results as arguments
        results[index] = arguments[1];
        if (!lock && pending === 0) {
          // When all parallel branches done, call the callback
          next.apply(null, results);
        }
      };
    };

    // Generates a callback generator for grouped results
    next.group = function () {
      var localCallback = next.parallel();
      var counter = 0;
      var pending = 0;
      var result = [];
      var error = undefined;

      function check() {
        if (pending === 0) {
          // When group is done, call the callback
          localCallback(error, result);
        }
      }
      process.nextTick(check); // Ensures that check is called at least once

      // Generates a callback for the group
      return function () {
        var index = counter++;
        pending++;
        return function () {
          pending--;
          // Compress the error from any result to the first argument
          if (arguments[0]) {
            error = arguments[0];
          }
          // Send the other results as arguments
          result[index] = arguments[1];
          if (!lock) { check(); }
        };
      };
    };

    // Start the engine an pass nothing to the first step.
    next();
  }

  // Tack on leading and tailing steps for input and output and return
  // the whole thing as a function.  Basically turns step calls into function
  // factories.
  Step.fn = function StepFn() {
    var steps = Array.prototype.slice.call(arguments);
    return function () {
      var args = Array.prototype.slice.call(arguments);

      // Insert a first step that primes the data stream
      var toRun = [function () {
        this.apply(null, args);
      }].concat(steps);

      // If the last arg is a function add it as a last step
      if (typeof args[args.length-1] === 'function') {
        toRun.push(args.pop());
      }


      Step.apply(null, toRun);
    }
  }

  module.exports = Step;
});

// // Hook into commonJS module systems
// if (typeof module !== 'undefined' && "exports" in module) {
//   module.exports = Step;
// }

// if(typeof define !== 'undefined')define(function(require, exports, module) {
//   module.exports = Step;
// });
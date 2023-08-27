
; (function () {
  // https://jsfiddle.net/nyh18bLq/
  function int(value) {
    return parseInt(value, 10)
  }

  /**
   * https://en.wikipedia.org/wiki/Collinearity
   * 
   * x=(x1+x2)/2
   * y=(y1+y2)/2
   */
  function checkCollinear(p0, p1, p2) {
    return int(p0.x + p2.x) === int(2 * p1.x) && int(p0.y + p2.y) === int(2 * p1.y);
  }

  function getDistance(p1, p2) {
    return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2))
  }

  function moveTo(to, from, radius) {
    var vector = { x: to.x - from.x, y: to.y - from.y };
    var length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    var unitVector = { x: vector.x / length, y: vector.y / length };

    return { x: from.x + unitVector.x * radius, y: from.y + unitVector.y * radius };
  }

  /**
   * Calculate the coordinate
   * 
   * @param  {number[]|object[]}  arr
   * @param  {object}             boundary
   * @return {object[]}
   */
  function genPoints(arr, ref, ref$1) {
    var minX = ref.minX;
    var minY = ref.minY;
    var maxX = ref.maxX;
    var maxY = ref.maxY;
    var max = ref$1.max;
    var min = ref$1.min;

    arr = arr.map(item => typeof item === 'number' ? item : item.value);
    var minValue = Math.min.apply(Math, arr.concat([min])) - 0.001;
    var gridX = (maxX - minX) / (arr.length - 1);
    var gridY = (maxY - minY) / (Math.max.apply(Math, arr.concat([max])) + 0.001 - minValue);

    return arr.map((value, index) => {
      return {
        x: index * gridX + minX,
        y: maxY - (value - minValue) * gridY +
          +(index === arr.length - 1) * 0.00001 -
          +(index === 0) * 0.00001
      }
    });
  }

  /**
   * From https://github.com/unsplash/react-trend/blob/master/src/helpers/DOM.helpers.js#L18
   */
  function genPath(points, radius) {
    var start = points.shift();

    return ("M" + start.x + " " + start.y +
      points.map((point, index) => {
        var next = points[index + 1];
        var prev = points[index - 1] || start;
        var isCollinear = next && checkCollinear(next, point, prev);

        if (!next || isCollinear)
          return ("L" + (point.x) + " " + (point.y))

        var threshold = Math.min(getDistance(prev, point), getDistance(next, point));
        var isTooCloseForRadius = threshold / 2 < radius;
        var radiusForPoint = isTooCloseForRadius ? threshold / 2 : radius;

        var before = moveTo(prev, point, radiusForPoint);
        var after = moveTo(next, point, radiusForPoint);

        return ("L" + (before.x) + " " + (before.y) + "S" + (point.x) + " " + (point.y) + " " + (after.x) + " " + (after.y))
      }).join('')
    )
  }

  var Path = {
    props: ['smooth', 'data', 'boundary', 'radius', 'id', 'max', 'min'],

    render(h) {
      var points = genPoints(this.data, this.boundary, { max: this.max, min: this.min });
      var d = genPath(points, this.smooth ? this.radius : 0);

      return h('path', {
        attrs: { d: d, fill: 'none', stroke: ("url(#" + this.id + ")") }
      })
    }
  };

  var Gradient = {
    props: ['gradient', 'gradientDirection', 'id'],
    render(h) {
      var gradient = this.gradient, gradientDirection = this.gradientDirection;
      var len = gradient.length - 1 || 1;
      var stops = gradient.slice().reverse().map(
        (color, index) => h('stop', { attrs: { offset: index / len, 'stop-color': color } })
      );

      return h('defs', [
        h('linearGradient', {
          attrs: {
            id: this.id,
            /*
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 1
            */
            x1: +(gradientDirection === 'left'), y1: +(gradientDirection === 'top'),
            x2: +(gradientDirection === 'right'), y2: +(gradientDirection === 'bottom')
          }
        },
          stops
        )
      ]);
    }
  };

  Vue.component("Trend", {
    name: 'Trend',
    props: {
      data: { type: Array, required: true },
      autoDraw: Boolean,
      autoDrawDuration: { type: Number, default: 2000 },
      autoDrawEasing: { type: String, default: 'ease' },
      gradient: { type: Array, default() { return ['#000']; } },
      gradientDirection: { type: String, default: 'top' },
      max: { type: Number, default: -Infinity },
      min: { type: Number, default: Infinity },
      height: Number,
      width: Number,
      padding: { type: Number, default: 8 },
      radius: { type: Number, default: 10 },
      smooth: Boolean
    },
    watch: {
      data: {
        immediate: true,
        handler(val) {
          this.$nextTick(() => {
            if (this.$isServer || !this.$refs.path || !this.autoDraw)
              return;

            var path = this.$refs.path.$el, length = path.getTotalLength();

            path.style.transition = 'none';
            path.style.strokeDasharray = length + ' ' + length;
            path.style.strokeDashoffset = Math.abs(length - (this.lastLength || 0));
            path.getBoundingClientRect();
            path.style.transition = "stroke-dashoffset " + (this.autoDrawDuration) + "ms " + (this.autoDrawEasing);
            path.style.strokeDashoffset = 0;
            this.lastLength = length;
          });
        }
      }
    },

    render(h) {
      if (!this.data || this.data.length < 2) return;

      var width = this.width;
      var height = this.height;
      var padding = this.padding;
      var viewWidth = width || 300;
      var viewHeight = height || 75;
      var boundary = {
        minX: padding,
        minY: padding,
        maxX: viewWidth - padding,
        maxY: viewHeight - padding
      };
      var props = this.$props;

      props.boundary = boundary;
      props.id = 'vue-trend-' + this._uid;

      return h('svg', { attrs: { width: width || '100%', height: height || '25%', viewBox: ("0 0 " + viewWidth + " " + viewHeight) } },
        [
          h(Gradient, { props: props }),
          h(Path, { props: props, ref: 'path' })
        ]
      )
    }
  });
})();
namespace aj.widget {
    var back2topTimerId: number = 0;

    /**
     *  回到顶部  <a href="###" @click="go">回到顶部</a>
     */
    export function back2top() {
        var top: number = 0;
        var speed: number = 0;

        back2topTimerId && window.clearInterval(back2topTimerId);
        back2topTimerId = window.setInterval(() => {
            top = document.documentElement.scrollTop || document.body.scrollTop;
            speed = Math.floor((0 - top) / 8);

            if (top === 0)
                clearInterval(back2topTimerId);
            else
                document.documentElement.scrollTop = document.body.scrollTop = top + speed;
        }, 20);
    }
}
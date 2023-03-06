import TabCommon from "./tab-common";
import FormPanel from "../meta/panel/form-config.vue";
import InputPane from "../meta/panel/input.vue";
import FormItemPane from "../meta/panel/form-item.vue";
import ButtonPane from "../meta/panel/button.vue";

export default {
    components: { FormPanel, InputPane, FormItemPane, ButtonPane },
    mixins: [TabCommon],
    data() {
        return {
            type: '',
            meta: {},
            props: {},
        };
    },
    watch: {
        props() {
            this.$parent.STAGE_RENDER.$forceUpdate();
        }
    }
}
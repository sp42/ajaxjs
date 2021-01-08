/**
 * 下拉列表
 */
Vue.component('aj-form-select', {
    template: `
        <select :name="name" class="aj-form-select">
            <template v-for="value, key, index in options">
                <option v-if="index === selectedIndex" selected :value="value" >{{key}}</option>
                <option v-else :value="value" >{{key}}</option>
            </template>
        </select>
    `,
    props: {
        name: { type: String, required: true }, 	// name for form
        options: { type: Object, required: true },	// JSON input data 
        selectedIndex: { type: Number }				// starts form 0
    }
});
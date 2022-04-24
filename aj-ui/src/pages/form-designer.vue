<template>
  <FormDesigner :init-meta-data="metaData" />
</template>

<script lang="ts">
import FormDesigner from "../components/form-designer/form-designer.vue";

export default {
  components: { FormDesigner },
  data(): any {
    return {
      metaData: [
        {
          type: "Form",
          children: [
            {
              type: "FormItem",
              children: [],
            },
          ],
        },
        {
          type: "Tabs",
          children: [
            {
              type: "TabPane",
              props: {
                label: "Tab1",
              },
              children: [
                {
                  type: "input_text",
                  input_type: "text",
                },
              ],
            },
            {
              type: "TabPane",
              props: {
                label: "Tab2",
              },
              children: [
                {
                  type: "Select",
                },
              ],
            },
          ],
        },
        {
          type: "input_text",
          input_type: "text",
        },
        {
          type: "Form",
          labelWidth: 120,
          children: [
            {
              type: "FormItem",
              props: {
                label: "id",
              },
              children: [
                {
                  type: "input_text",
                  key: "id",
                  input_type: "text",
                },
                {
                  type: "Select",
                  key: "id",
                },
                {
                  type: "Slider",
                  key: "id",
                },
              ],
            },
            {
              type: "FormItem",
              props: {
                label: "name",
              },
              children: [
                {
                  type: "Button",
                  text: "点击我",
                  key: "id",
                  props: {
                    type: "primary",
                  },
                },
                {
                  type: "text",
                  text: "我是一段文本",
                  isHtmlTag: true,
                },
              ],
            },
            {
              type: "FormItem",
              label: "content",
              props: {
                label: "content",
              },
              children: [
                {
                  type: "input_textarea",
                  key: "id",
                },
                {
                  type: "input_password",
                  key: "id",
                },
              ],
            },
          ],
        },
        {
          type: "Form",
          children: [
            {
              type: "FormItem",
              props: {
                label: "id2",
              },
              children: [
                {
                  type: "input_text",
                  key: "id",
                },
                {
                  type: "input_text",
                  key: "id",
                },
              ],
            },
            {
              type: "FormItem",

              props: {
                label: "name2",
              },
              children: [
                {
                  type: "input_text",
                  key: "id",
                },
                {
                  type: "input_text",
                  key: "id",
                },
              ],
            },
            {
              type: "FormItem",
              props: {
                label: "content2",
              },
              children: [
                {
                  type: "Row",
                  children: [
                    {
                      type: "Col",
                      children: [
                        {
                          type: "TimePicker",
                          key: "id",
                        },
                        {
                          type: "DatePicker",
                          key: "id2",
                        },
                      ],
                    },
                    {
                      type: "Col",
                      children: [
                        {
                          type: "ColorPicker",
                          key: "id3",
                        },
                      ],
                    },
                  ],
                },
                {
                  type: "Row",
                  children: [],
                },
              ],
            },
          ],
        },
      ],
    };
  },
};
</script>

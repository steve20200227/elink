<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'iotDoc',
  data() {
    return {
      url: process.env.VUE_APP_DOC_URL,
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>

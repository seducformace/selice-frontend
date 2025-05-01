<template>
  <div id="app">
    <Header v-if="shouldShowHeader" :key="$route.fullPath" />
    <router-view />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';

export default {
  name: 'App',
  components: {
    Header,
  },
  computed: {
    shouldShowHeader() {
      const hiddenRoutes = ['/login'];
      return !hiddenRoutes.includes(this.$route.path);
    },
  },
};
</script>

<style>
/* Estilos globais, se necessário */
</style>

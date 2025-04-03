<template>
  <div class="dashboard-card" @click="navigateToRoute">
    <img :src="icon" alt="Card Icon" class="card-icon" />
    <h3>{{ title }}</h3>
    <p>{{ count }}</p>
  </div>
</template>
<script>
export default {
  name: 'DashboardCard',
  props: {
    title: { type: String, required: true },
    icon: { type: String, required: true },
    count: { type: [String, Number], required: true },
    route: { type: String, required: true },
  },
  methods: {
    navigateToRoute() {
      this.$router.push(this.route);
    },
  },
};
</script>
<style scoped>
.dashboard-card {
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}
.dashboard-card:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}
.card-icon {
  width: 50px;
  height: 50px;
  margin-bottom: 10px;
}
h3 {
  margin: 0;
  font-size: 18px;
}
p {
  margin: 5px 0 0;
  font-size: 16px;
  font-weight: bold;
}
</style>

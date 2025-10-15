<script setup>
import { RouterLink, RouterView } from 'vue-router'

const socket = new WebSocket('ws://' + import.meta.env.VITE_BACKEND_SOCKET + '/ws/dummy');
socket.onopen = () => {
  console.log('WebSocket connection established');
};

</script>

<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/special-ops">Special Ops</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<style scoped></style>

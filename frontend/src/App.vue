<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { ref, provide } from 'vue';

const socket = new WebSocket('ws://' + import.meta.env.VITE_BACKEND_SOCKET + '/ws');
socket.onopen = () => {
  console.log('WebSocket connection established');
};

const tableData = ref([]);


socket.onmessage = (event) => {
  const message = JSON.parse(event.data);
  switch (message.type) {
    case 'DUMMY_CREATED':
      console.log('New dummy created:', message.data);
      tableData.value.push(message.data);
      break;
  }
};

provide('tableData', tableData);
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

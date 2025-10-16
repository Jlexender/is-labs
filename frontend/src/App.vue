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
    case 'DUMMY_DELETED':
      console.log('Dummy deleted:', message.data);
      tableData.value = tableData.value.filter(item => item.id !== parseInt(message.data));
      break;
    case 'DUMMY_UPDATED':
      console.log('Dummy updated:', message.data);
      tableData.value = tableData.value.map(item => item.id === message.data.id ? message.data : item);
      break;
  }
};

provide('tableData', tableData);
</script>

<template>
  <div class="app-container">
    <header>
      <div class="wrapper">
        <nav>
          <RouterLink to="/">Home</RouterLink>
          <RouterLink to="/special-ops">Special Ops</RouterLink>
        </nav>
      </div>
    </header>

    <main class="view-wrapper">
      <div class="view-card">
        <RouterView />
      </div>
    </main>
  </div>
</template>

<style scoped></style>

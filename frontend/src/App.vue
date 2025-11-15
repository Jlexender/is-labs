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
    case 'TICKET_CREATED':
      console.log('Ticket created:', message.data);
      tableData.value.push(message.data);
      break;
    case 'TICKET_DELETED':
      console.log('Ticket deleted:', message.data);
      tableData.value = tableData.value.filter(item => item.id !== parseInt(message.data));
      break;
    case 'TICKET_UPDATED':
      console.log('Ticket updated:', message.data);
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
          <RouterLink to="/import">Import</RouterLink>
          <RouterLink to="/import-history">Import History</RouterLink>
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

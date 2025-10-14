<script setup>
import { onMounted, ref } from 'vue';

const count = ref(null);

onMounted(async () => {
    try {
        const response = await fetch('http://localhost:8080/api/dummy/count');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        count.value = await response.json();
    } catch (error) {
        console.error('Error fetching entity count:', error);
    }
});

</script>

<template>
    <div class="home">
        <h1>Home View</h1>
        <p>Welcome to the Home View!</p>
        <p>Number of entities: {{ count }}</p>
    </div>
</template>

<style scoped></style>
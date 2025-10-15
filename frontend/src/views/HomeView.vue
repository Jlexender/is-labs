<script setup>
import EntityPanel from '@/components/EntityTable.vue';
import { ref, onMounted } from 'vue';

const entityCount = ref(null);

onMounted(async () => {
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/count');
        entityCount.value = await response.json();
    } catch (error) {
        console.error('Error fetching entity count:', error);
    }
});
</script>

<template>
    <div class="home">
        <h1>Home View</h1>
        <EntityPanel />

        <p>Entity count: {{ entityCount }}</p>
    </div>
</template>

<style scoped></style>
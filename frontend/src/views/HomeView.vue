<script setup>
import EntityTable from '@/components/EntityTable.vue';
import { ref, onMounted } from 'vue';

const entityCount = ref(null);

onMounted(async () => {
    try {
        const response = await fetch('/api/dummy/count');
        entityCount.value = await response.json();
    } catch (error) {
        console.error('Error fetching entity count:', error);
    }
});
</script>

<template>
    <div class="home">
        <h1>Home View</h1>
        <EntityTable />

        <p>Entity count: {{ entityCount }}</p>
    </div>
</template>

<style scoped></style>
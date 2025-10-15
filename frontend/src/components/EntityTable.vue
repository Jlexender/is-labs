<script setup>
import { onMounted, inject } from 'vue';

const tableData = inject('tableData');

onMounted(async () => {
    try {
        const response = await fetch('http://' + import.meta.env.VITE_BACKEND_SOCKET + '/api/dummy/all');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        tableData.value = await response.json();

        console.log('Fetched data:', tableData.value);
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
});
</script>

<template>
    <div class="entity-panel">
        <h2>Dummy Table</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Created at</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="dummy in tableData" :key="dummy.id">
                    <td>{{ dummy.id }}</td>
                    <td>{{ dummy.name }}</td>
                    <td>{{ dummy.createdAt }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped></style>
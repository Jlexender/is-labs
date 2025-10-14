<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentTime = ref(new Date().toLocaleTimeString())

let intervalId = null

onMounted(() => {
    intervalId = setInterval(() => {
        currentTime.value = new Date().toLocaleTimeString()
    }, 1000)
})

onUnmounted(() => {
    clearInterval(intervalId)
})
</script>

<template>
    <div class="special-ops">
        <p>Current time: {{ currentTime }}</p>
    </div>
</template>

<style scoped>
</style>

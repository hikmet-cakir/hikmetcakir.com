import { defineConfig } from 'astro/config';
 
export default defineConfig({ 
  output: 'server', 
  middleware: true,
  server: {
    port: 4001
  } 
});

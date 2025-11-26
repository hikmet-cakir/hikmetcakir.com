import { defineConfig } from 'astro/config';
 
export default defineConfig({ 
  output: 'server',
  adapter: node(),
  middleware: true,
  server: {
    port: 4001
  } 
});

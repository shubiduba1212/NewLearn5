/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        background: "var(--background)",
        foreground: "var(--foreground)",
        main: '#6463AF',
        point: '#44478F',
      },
      fontSize: {
        'font-basic' : '1rem',
        'font_28' : '28px',
        'font_24' : '24px',
        'font_20' : '20px',
        'font_18' : '18px',
        'font_16' : '16px',
        'font_14' : '14px',
      },
      width:{
        'w-100':'200px',
        'w-40':'400px',
        'w-100':'500px',
      },
      height:{
        'h-40': '40px',        
      }
    },
  },
  plugins: [],
};

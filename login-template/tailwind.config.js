/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    '../src/main/resources/templates/**/*.{html,thymeleaf}',
    '../src/main/resources/static/**/*.{html,js}'
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          ink: '#15395F',
          mint: '#57B886',
          sand: '#F7F7F2',
          flame: '#F86624',
          crimson: '#EA3546'
        }
      },
      fontFamily: {
        display: ['Poppins', 'ui-sans-serif', 'system-ui']
      }
    }
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
};

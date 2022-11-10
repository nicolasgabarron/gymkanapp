FROM node:16.17.0 AS frontend

WORKDIR /var/tmp

ADD package.json .
ADD package-lock.json .

ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true

RUN npm install

RUN npm install --global @angular/cli

RUN npx ngcc --properties es2015 browser module main --first-only --create-ivy-entry-points

ADD . .

RUN npm run build -- -c production



FROM nginx:stable

COPY --from=frontend /var/tmp/target/classes/static /usr/share/nginx/html

COPY --from=frontend /var/tmp/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

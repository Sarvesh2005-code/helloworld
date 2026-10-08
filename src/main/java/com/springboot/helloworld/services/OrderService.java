package com.springboot.helloworld.services;

import com.springboot.helloworld.config.OrderWebSocketHandler;
import com.springboot.helloworld.dto.CreateOrderRequest;
import com.springboot.helloworld.model.Order;
import com.springboot.helloworld.model.OrderStatus;
import com.springboot.helloworld.repository.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderWebSocketHandler webSocketHandler;

    private final InventoryService inventoryService;


    public OrderService(
            OrderRepository orderRepository,
            OrderWebSocketHandler webSocketHandler,
            InventoryService inventoryService) {

        this.orderRepository = orderRepository;

        this.webSocketHandler = webSocketHandler;

        this.inventoryService = inventoryService;
    }


    // ==========================================
    // CREATE ORDER
    // ==========================================

    public Order createOrder(
            CreateOrderRequest request) {

        Order order = new Order(
                request.getCustomerName(),
                request.getProductName(),
                request.getQuantity()
        );

        Order savedOrder =
                orderRepository.save(order);


        // Notify connected clients
        webSocketHandler.sendOrder(
                savedOrder
        );


        // Start processing asynchronously
        processOrder(
                savedOrder.getId()
        );


        return savedOrder;
    }


    // ==========================================
    // ORDER PROCESSING
    // ==========================================

    private void processOrder(
            Long orderId) {

        new Thread(() -> {

            try {

                Order order =
                        getOrderById(orderId);


                // --------------------------------
                // STEP 1: INVENTORY CHECK
                // --------------------------------

                order.setStatus(
                        OrderStatus.INVENTORY_CHECK
                );

                orderRepository.save(order);

                webSocketHandler.sendOrder(order);


                Thread.sleep(1500);


                // Try to reserve stock

                boolean stockReserved =
                        inventoryService.reserveStock(
                                order.getProductName(),
                                order.getQuantity()
                        );


                // --------------------------------
                // INVENTORY FAILED
                // --------------------------------

                if (!stockReserved) {

                    order.setStatus(
                            OrderStatus.FAILED
                    );

                    orderRepository.save(order);

                    webSocketHandler.sendOrder(
                            order
                    );

                    System.out.println(
                            "Order #" +
                                    order.getId() +
                                    " failed: insufficient stock"
                    );

                    return;
                }


                System.out.println(
                        "Stock reserved for Order #" +
                                order.getId()
                );


                // --------------------------------
                // STEP 2: PAYMENT VERIFICATION
                // --------------------------------

                order.setStatus(
                        OrderStatus.PAYMENT_VERIFICATION
                );

                orderRepository.save(order);

                webSocketHandler.sendOrder(order);


                Thread.sleep(1500);


                /*
                 * Payment is simulated.
                 *
                 * In a real application this would
                 * communicate with a payment service.
                 */

                boolean paymentSuccessful =
                        verifyPayment(order);


                // --------------------------------
                // PAYMENT FAILED
                // --------------------------------

                if (!paymentSuccessful) {


                    /*
                     * Important:
                     *
                     * Payment failed, so release
                     * the stock that we reserved.
                     */

                    inventoryService.releaseStock(
                            order.getProductName(),
                            order.getQuantity()
                    );


                    order.setStatus(
                            OrderStatus.FAILED
                    );

                    orderRepository.save(order);

                    webSocketHandler.sendOrder(
                            order
                    );


                    System.out.println(
                            "Order #" +
                                    order.getId() +
                                    " failed: payment"
                    );


                    return;
                }


                // --------------------------------
                // STEP 3: CONFIRM ORDER
                // --------------------------------

                order.setStatus(
                        OrderStatus.CONFIRMED
                );

                orderRepository.save(order);

                webSocketHandler.sendOrder(order);


                System.out.println(
                        "Order #" +
                                order.getId() +
                                " confirmed"
                );


            }
            catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(
                        "Order processing interrupted: "
                                + orderId
                );

            }
            catch (Exception e) {

                System.out.println(
                        "Order processing error: "
                                + e.getMessage()
                );

            }

        }).start();
    }


    // ==========================================
    // PAYMENT VERIFICATION
    // ==========================================

    private boolean verifyPayment(
            Order order) {

        System.out.println(
                "Verifying payment for Order #" +
                        order.getId()
        );


        /*
         * Simulated successful payment.
         *
         * Replace this with a real payment
         * service in a production system.
         */

        return true;
    }


    // ==========================================
    // GET ALL ORDERS
    // ==========================================

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    // ==========================================
    // GET ORDER
    // ==========================================

    public Order getOrderById(
            Long id) {

        return orderRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Order not found"
                        )
                );
    }


    // ==========================================
    // MANUAL STATUS UPDATE
    // ==========================================

    public Order updateStatus(
            Long id,
            OrderStatus status) {

        Order order =
                getOrderById(id);

        order.setStatus(status);

        Order updatedOrder =
                orderRepository.save(order);

        webSocketHandler.sendOrder(
                updatedOrder
        );

        return updatedOrder;
    }


    // ==========================================
    // CANCEL ORDER
    // ==========================================

    public Order cancelOrder(
            Long id) {

        Order order =
                getOrderById(id);


        /*
         * Don't allow cancellation after
         * the order has already been confirmed.
         */

        if (
                order.getStatus() ==
                        OrderStatus.CONFIRMED
        ) {

            throw new RuntimeException(
                    "Confirmed orders cannot be cancelled"
            );
        }


        /*
         * Release stock if it was reserved.
         *
         * For PENDING orders this simply does
         * nothing because no stock was reserved.
         */

        if (
                order.getStatus() ==
                        OrderStatus.PAYMENT_VERIFICATION
                        ||
                        order.getStatus() ==
                                OrderStatus.INVENTORY_CHECK
        ) {

            inventoryService.releaseStock(
                    order.getProductName(),
                    order.getQuantity()
            );
        }


        order.setStatus(
                OrderStatus.CANCELLED
        );


        Order cancelledOrder =
                orderRepository.save(order);


        webSocketHandler.sendOrder(
                cancelledOrder
        );


        return cancelledOrder;
    }

}